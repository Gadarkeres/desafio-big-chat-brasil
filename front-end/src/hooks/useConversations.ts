import { useQuery } from '@tanstack/react-query'
import { getConversations } from '../services/conversationService'

export const conversationsQueryKey = ['conversations'] as const

export function useConversations() {
  return useQuery({
    queryKey: conversationsQueryKey,
    queryFn: getConversations,
  })
}
