import { SessionService } from './session-service';

const BASE_URL = import.meta.env.VITE_APP_BACKEND_URL;
const sessionService = new SessionService(`${BASE_URL}/authentication`, 'session_jwt');

export default sessionService;
