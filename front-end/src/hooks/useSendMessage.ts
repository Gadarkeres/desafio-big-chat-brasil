import { useMutation, useQueryClient } from '@tanstack/react-query'
import { conversationsQueryKey } from './useConversations'
import { conversationMessagesQueryKey } from './useConversationMessages'
import { updateStoredClient } from '../services/authStorage'
import { sendMessage } from '../services/messageService'
import type { SendMessageRequest } from '../types/chat'

export function useSendMessage() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (payload: SendMessageRequest) => sendMessage(payload),
    onSuccess: (response, payload) => {
      queryClient.invalidateQueries({
        queryKey: conversationMessagesQueryKey(payload.conversationId),
      })
      queryClient.invalidateQueries({ queryKey: conversationsQueryKey })

      if (response.currentBalance !== undefined) {
        updateStoredClient({ balance: response.currentBalance })
      }
    },
  })
}
