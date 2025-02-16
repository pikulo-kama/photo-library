import { Box, Divider, useMediaQuery } from "@mui/material";
import { useUploadImageMutation } from "../api/attachmentSlice";
import { useNavigate, useParams } from "react-router-dom";
import UploadButton from "../components/library/button/UploadButton";
import ImageGrid from "../components/library/ImageGrid";
import DirectoryBar from "../components/library/DirectoryBar";
import { useDeleteDirectoryMutation, useGetDirectoriesQuery } from "../api/directorySlice";
import DirectoryPath from "../components/library/DirectoryPath";
import CreateDirectoryFormField from "../components/library/CreateDirectoryFormField";
import DeleteDirectoryButton from "../components/library/button/DeleteDirectoryButton";


const LibraryPage = () => {

    const isMobile = useMediaQuery(theme => theme.breakpoints.down("md"));

    const navigate = useNavigate();
    const { directory } = useParams();
    const { data: directoryDetails, isLoading } = useGetDirectoriesQuery(directory);

    const [ uploadImage ] = useUploadImageMutation();
    const [ deleteDirectory ] = useDeleteDirectoryMutation();
    
    const onFileUpload = async (file) => {
        await uploadImage({file, directory}).unwrap();
    };

    const onDirectoryDelete = async () => {

        let parentDirectoryURL = '/library';

        const directoryPath = directoryDetails.response.directoryPath;
        const currentDirectoryIndex = directoryPath.findIndex(dir => dir.directoryUUID == directory);
        
        // If it's root directory then just refresh the page.
        if (currentDirectoryIndex > 0) {
            const parentDirectory = directoryPath[currentDirectoryIndex - 1];
            parentDirectoryURL += `/${parentDirectory.directoryUUID}`;
        }

        await deleteDirectory(directory).unwrap();
        navigate(parentDirectoryURL);
    }

    if (isLoading) {
        return;
    }

    let i = 0;
    let directories = [];
    const directoryPath = directoryDetails.response.directoryPath;
    
    while (i < 10) {
        directories = directories.concat(directoryDetails.response.directories);
        i++;
    }

    return (
        <>
            <Box 
                display="flex"
                gap={1}
                sx={{
                    py: 2,
                    pl: 2,
                    position: "sticky",
                    top: "64px",
                    bgcolor: "primary.dark"
                }}
            >
                <UploadButton 
                    onUpload={onFileUpload} 
                    title="Upload" 
                />
                <DeleteDirectoryButton 
                    onDelete={onDirectoryDelete}
                    title="Delete Directory"
                />
            </Box>
            <Box
                sx={{
                    top: "126px",
                    position: "sticky"
                }}
            >
                <DirectoryPath directoryPath={directoryPath} />
            </Box>
            <Box
                sx={{
                    overflow: 'hidden',
                    width: '100vw',
                    height: 'calc(100vh - 200px)',
                    mt: "100px",
                    display: "flex",
                    flexDirection: "row",
                    gap: "2rem"
                }}
            >
                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        width: isMobile ? "34vw" : "15vw"
                    }}
                >
                    <CreateDirectoryFormField />
                    <Box
                        sx={{
                            overflowX: 'hidden'
                        }}
                    >
                        <DirectoryBar directories={directories} />
                    </Box>
                </Box>
                <Divider sx={{ width: '1px', bgcolor: 'primary.main'}} />
                <Box
                    sx={{
                        overflowX: 'hidden',
                        width: isMobile ? "66vw" : "85vw"
                    }}
                >
                    <ImageGrid />
                </Box>
            </Box>
        </>
    );
}

export default LibraryPage;
