import { configureStore } from "@reduxjs/toolkit";
import { setupListeners } from "@reduxjs/toolkit/query";
import { photoLibraryApi } from "./api/photoLibraryApi";


export const store = configureStore({
    reducer: {
        [photoLibraryApi.reducerPath]: photoLibraryApi.reducer
    },
    middleware: getDefaultMiddleware =>
        getDefaultMiddleware().concat(photoLibraryApi.middleware)
});

setupListeners(store.dispatch);
