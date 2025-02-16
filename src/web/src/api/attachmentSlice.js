import { photoLibraryApi } from "./photoLibraryApi";


export const libraryTag = "photos";

export const attachmentSlice = photoLibraryApi.injectEndpoints({
    endpoints: builder => ({
        getImages: builder.query({
            query: (directory) => directory ? `/attachment/${directory}` : `/attachment`,
            providesTags: [libraryTag]
        }),
        uploadImage: builder.mutation({
            query: (data) => {

                const {
                    file,
                    directory
                } = data;

                const formData = new FormData();
                formData.append('file', file);

                if (directory) {
                    formData.append('directory', directory);
                }

                return {
                    url: '/attachment',
                    method: 'POST',
                    body: formData
                };
            },
            invalidatesTags: [libraryTag]
        }),
        deleteImage: builder.mutation({
            query: (body) => ({
                url: '/attachment',
                method: 'DELETE',
                body
            }),
            invalidatesTags: [libraryTag]
        })
    })
});

export const {
    useGetImagesQuery,
    useUploadImageMutation,
    useDeleteImageMutation
} = attachmentSlice;
