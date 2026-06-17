import { useMutation } from '@tanstack/react-query'
import { Button, Card, Form, Input, Select } from 'antd'
import Cookies from 'js-cookie'
import { useNavigate } from 'react-router-dom'
import { AUTH_TOKEN_KEY } from '../../config/auth'
import { authenticateClient } from '../../services/authService'
import { useNotificationService } from '../../services/notificationService'
import type { AuthRequest } from '../../types/auth'

const DOCUMENT_TYPE_OPTIONS = [
  { label: 'CPF', value: 'CPF' },
  { label: 'CNPJ', value: 'CNPJ' },
]

export function LoginForm() {
  const navigate = useNavigate()
  const notify = useNotificationService()

  const { mutate, isPending } = useMutation({
    mutationFn: authenticateClient,
    onSuccess: (response) => {
      Cookies.set(AUTH_TOKEN_KEY, response.token, { sameSite: 'strict' })

      notify({
        type: 'success',
        message: 'Login realizado',
        description: `Bem-vindo, ${response.client.name}.`,
      })

      navigate('/conversations', { replace: true })
    },
  })

  const handleSubmit = (values: AuthRequest) => {
    mutate(values)
  }

  return (
    <Card className="login-card" variant="borderless">
      <div className="login-card__header">
        <h1 className="login-card__title">Big Chat Brasil</h1>
        <p className="login-card__subtitle">
          Bem-vindo de volta, Informe o documento de identificação para acessar as conversas.
        </p>
      </div>

      <Form<AuthRequest>
        layout="vertical"
        initialValues={{ documentType: 'CPF' }}
        onFinish={handleSubmit}
        requiredMark={false}
      >
        <Form.Item
          label="Tipo de documento"
          name="documentType"
          rules={[{ required: true, message: 'Selecione o tipo de documento' }]}
        >
          <Select options={DOCUMENT_TYPE_OPTIONS} size="large" />
        </Form.Item>

        <Form.Item
          label="Documento"
          name="documentId"
          rules={[{ required: true, message: 'Informe o documento' }]}
        >
          <Input size="large" placeholder="CPF ou CNPJ" autoComplete="off" />
        </Form.Item>

        <Button type="primary" htmlType="submit" size="large" block loading={isPending}>
          Entrar
        </Button>
      </Form>
    </Card>
  )
}
