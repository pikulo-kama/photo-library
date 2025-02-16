import { photoLibraryApi } from "./photoLibraryApi";


export const authSlice = photoLibraryApi.injectEndpoints({
    endpoints: builder => ({
        getUserInfo: builder.query({
            query: () => "/auth/user-info"
        })
    })
});

export const {
    useGetUserInfoQuery
} = authSlice;
