import Cookies from 'js-cookie'
import { AUTH_CLIENT_KEY, AUTH_TOKEN_KEY } from '../config/auth'
import type { AuthClient } from '../types/auth'

export const AUTH_CLIENT_UPDATED_EVENT = 'bcb_client_updated'

function notifyClientUpdated() {
  window.dispatchEvent(new Event(AUTH_CLIENT_UPDATED_EVENT))
}

export function getAuthToken() {
  return Cookies.get(AUTH_TOKEN_KEY)
}

export function getStoredClient() {
  const storedClient = localStorage.getItem(AUTH_CLIENT_KEY)

  if (!storedClient) {
    return null
  }

  try {
    return JSON.parse(storedClient) as AuthClient
  } catch {
    localStorage.removeItem(AUTH_CLIENT_KEY)
    return null
  }
}

export function setAuthSession(token: string, client: AuthClient) {
  Cookies.set(AUTH_TOKEN_KEY, token, { sameSite: 'strict' })
  localStorage.setItem(AUTH_CLIENT_KEY, JSON.stringify(client))
  notifyClientUpdated()
}

export function updateStoredClient(clientUpdates: Partial<AuthClient>) {
  const currentClient = getStoredClient()

  if (!currentClient) {
    return
  }

  localStorage.setItem(AUTH_CLIENT_KEY, JSON.stringify({ ...currentClient, ...clientUpdates }))
  notifyClientUpdated()
}

export function clearAuthSession() {
  Cookies.remove(AUTH_TOKEN_KEY)
  localStorage.removeItem(AUTH_CLIENT_KEY)
  notifyClientUpdated()
}
