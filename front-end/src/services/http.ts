import axios from 'axios'
import { clearAuthSession, getAuthToken } from './authStorage'
import { notify } from './notificationService'

const API_BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

type ApiErrorResponse = {
  message?: string
  details?: string[]
}

export const http = axios.create({
  baseURL: API_BASE_URL,
})

http.interceptors.request.use((config) => {
  const token = getAuthToken()

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

http.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status
    const apiError = error.response?.data as ApiErrorResponse | undefined

    if (status === 400) {
      notify({
        type: 'warning',
        message: apiError?.message ?? 'Dados inválidos',
        description: apiError?.details?.length ? apiError.details.join('\n') : undefined,
      })
    }

    if (status === 401) {
      const isLoginPage = window.location.pathname === '/login'

      notify({
        type: 'warning',
        message: isLoginPage ? 'Tipo de documento ou registro incorretos' : 'Sessão expirada, por favor faça login novamente',
        description: isLoginPage
          ? 'Verifique os dados informados e tente novamente.'
          : 'Faça login novamente para continuar.',
      })

      if (!isLoginPage) {
        clearAuthSession()
        window.location.replace('/login')
      }
    }

    if (status >= 500) {
      notify({
        type: 'error',
        message: 'Erro no servidor',
        description: 'Não foi possivel concluir a operação. Tente novamente mais tarde.',
      })
    }

    return Promise.reject(error)
  },
)
