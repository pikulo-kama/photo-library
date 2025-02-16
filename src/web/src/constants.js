
export const REACT_APP_BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
export const REACT_APP_BACKEND_URL_V1 = `${REACT_APP_BACKEND_URL}/rest/v1`;
export const getOauth2URL = (provider) => `${REACT_APP_BACKEND_URL}/oauth2/authorization/${provider}`;
