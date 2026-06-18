import { format, isToday, isYesterday, parseISO } from 'date-fns'

function parseApiDate(value: string) {
  const parsedDate = parseISO(value)

  if (Number.isNaN(parsedDate.getTime())) {
    return null
  }

  return parsedDate
}

export function formatMessageTime(value: string) {
  const date = parseApiDate(value)

  if (!date) {
    return value
  }

  return format(date, 'HH:mm')
}

export function formatConversationTime(value: string) {
  const date = parseApiDate(value)

  if (!date) {
    return value
  }

  if (isToday(date)) {
    return format(date, 'HH:mm')
  }

  if (isYesterday(date)) {
    return 'Ontem'
  }

  return format(date, 'dd/MM/yyyy')
}
