import fetchIntercept from 'fetch-intercept';
import sessionService from '@/service/session-singleton';

const unregister = fetchIntercept.register({
  request(url, options) {
    const token = sessionService.currentToken;
    if (!token) return [url, options];

    const newOptions = options ? { ...options } : { headers: {} };
    newOptions.headers = {
      ...newOptions.headers,
      'Authorization': token
    };
    return [url, newOptions];
  },

  response(response) {
    const newToken = response.headers.get('Authorization');
    if (newToken && sessionService.currentAccount) {
      sessionService.saveSession(newToken, sessionService.currentAccount);
    }

    if (response.status === 401) {
      sessionService.logout();
      window.location.href = '/login';
    }

    return response;
  },

  requestError(error) {
    console.error('Request error:', error);
    return Promise.reject(error);
  },

  responseError(error) {
    console.error('Response error:', error);
    return Promise.reject(error);
  }
});

export default unregister;
