import Cookies from 'js-cookie'
import {
  BrowserRouter,
  Navigate,
  Outlet,
  Route,
  Routes,
} from 'react-router-dom'
import { ChatPage } from '../pages/ChatPage'
import { LoginPage } from '../pages/LoginPage'

const AUTH_TOKEN_KEY = 'bcb_token'

function PrivateRoute() {
  const token = Cookies.get(AUTH_TOKEN_KEY)

  if (!token) {
    return <Navigate to="/login" replace />
  }

  return <Outlet />
}

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route element={<PrivateRoute />}>
          <Route path="/" element={<Navigate to="/conversations" replace />} />
          <Route path="/conversations" element={<ChatPage />} />
          <Route path="/conversations/:conversationId" element={<ChatPage />} />
        </Route>

        <Route path="*" element={<Navigate to="/conversations" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
