import { http } from './http'
import type { ChatMessage, SendMessageRequest, SendMessageResponse } from '../types/chat'

export async function getConversationMessages(conversationId: string) {
  const { data } = await http.get<ChatMessage[]>(`/conversations/${conversationId}/messages`)
  return data
}

export async function sendMessage(payload: SendMessageRequest) {
  const { data } = await http.post<SendMessageResponse>('/messages', payload)
  return data
}
