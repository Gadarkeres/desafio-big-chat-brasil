import { http } from './http'
import type { AuthRequest, AuthResponse } from '../types/auth'

export async function authenticateClient(payload: AuthRequest) {
  const { data } = await http.post<AuthResponse>('/auth', payload)
  return data
}
