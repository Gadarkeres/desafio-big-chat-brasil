import { notification } from 'antd'
import type { ArgsProps } from 'antd/es/notification'

const DEFAULT_PLACEMENT: ArgsProps['placement'] = 'bottomLeft'

export const notify = (args: ArgsProps): void => {
  notification[args.type || 'success']({
    placement: DEFAULT_PLACEMENT,
    ...args,
  })
}

export const useNotificationService = () => notify
