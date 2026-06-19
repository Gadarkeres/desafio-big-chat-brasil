import { useQuery } from '@tanstack/react-query'
import { getConversationMessages } from '../services/messageService'

export const conversationMessagesQueryKey = (conversationId?: string) =>
  ['conversations', conversationId, 'messages'] as const

export function useConversationMessages(conversationId?: string) {
  return useQuery({
    queryKey: conversationMessagesQueryKey(conversationId),
    queryFn: () => getConversationMessages(conversationId!),
    enabled: Boolean(conversationId),
    refetchInterval: 3000,
  })
}
