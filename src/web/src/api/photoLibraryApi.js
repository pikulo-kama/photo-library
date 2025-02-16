import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { REACT_APP_BACKEND_URL_V1 } from "../constants";

export const photoLibraryApi = createApi({
    reducerPath: "photoLibraryApi",
    baseQuery: fetchBaseQuery({
        baseUrl: REACT_APP_BACKEND_URL_V1,
        credentials: "include"
    }),
    endpoints: builder => ({})
});