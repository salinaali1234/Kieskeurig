// service/fetch-client.ts
export async function fetchWithAuth(
  input: RequestInfo,
  init: RequestInit = {}
) {
  const token = sessionStorage.getItem('session_jwt_token')
  const headers = new Headers(init.headers ?? {})
  if (token) {
    headers.set('Authorization', token)
  }
  headers.set('Content-Type', 'application/json')
  return fetch(input, {
    ...init,
    credentials: 'include',
    headers,
  })
}
