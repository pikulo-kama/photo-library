import { photoLibraryApi } from "./photoLibraryApi";


export const directoryTag = "directories";

export const directorySlice = photoLibraryApi.injectEndpoints({
    endpoints: builder => ({
        getDirectories: builder.query({
            query: (directory) => directory ? `/directory/${directory}` : `/directory`,
            providesTags: [directoryTag]
        }),
        createDirectory: builder.mutation({
            query: ({directoryName, parentDirectoryUUID}) => ({

                url: '/directory',
                method: 'POST',
                body: {
                    directoryName,
                    parentDirectoryUUID,
                }
            }),
            invalidatesTags: [directoryTag]
        }),
        deleteDirectory: builder.mutation({
            query: (directory) => ({
                url: '/directory',
                method: 'DELETE',
                body: {
                    directoryUUID: directory
                }
            }),
            invalidatesTags: [directoryTag]
        })
    })
});

export const {
    useGetDirectoriesQuery,
    useCreateDirectoryMutation,
    useDeleteDirectoryMutation
} = directorySlice;
