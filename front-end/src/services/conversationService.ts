import { http } from './http'
import type { ConversationSummary } from '../types/chat'

export async function getConversations() {
  const { data } = await http.get<ConversationSummary[]>('/conversations')
  return data
}
