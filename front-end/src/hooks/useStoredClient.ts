import { useEffect, useState } from 'react'
import {
  AUTH_CLIENT_UPDATED_EVENT,
  getStoredClient,
} from '../services/authStorage'

export function useStoredClient() {
  const [client, setClient] = useState(getStoredClient)

  useEffect(() => {
    function refreshClient() {
      setClient(getStoredClient())
    }

    window.addEventListener(AUTH_CLIENT_UPDATED_EVENT, refreshClient)
    window.addEventListener('storage', refreshClient)

    return () => {
      window.removeEventListener(AUTH_CLIENT_UPDATED_EVENT, refreshClient)
      window.removeEventListener('storage', refreshClient)
    }
  }, [])

  return client
}
