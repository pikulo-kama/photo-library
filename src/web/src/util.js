
export const getLocalStorageObject = (key, defaultValue) => JSON.parse(localStorage.getItem(key)) ?? defaultValue;
export const setLocalStorageObject = (key, value) => localStorage.setItem(key, JSON.stringify(value));
