import { AddBoxSharp } from '@mui/icons-material';
import { Box, IconButton, TextField } from '@mui/material';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useCreateDirectoryMutation } from '../../api/directorySlice';


const CreateDirectoryFormField = () => {

    const { directory } = useParams();
    const [ directoryName, setDirectoryName ] = useState("");
    const [ createDirectory ] = useCreateDirectoryMutation();
    
    const onDirectoryCreate = async () => {

        await createDirectory({
            directoryName: directoryName, 
            parentDirectoryUUID: directory
        }).unwrap();

        setDirectoryName("");
    };

    return (
        <Box
            sx={{ 
                display: "flex",
                alignItems: "flex-end",
                gap: 2,
                px: 2,
                pb: 1
            }}
        >
            <TextField
                onChange={(event) => setDirectoryName(event.target.value)}
                value={directoryName}
                label="Add Directory"
                variant="standard" 
            />
            <IconButton
                size="large"
                color="primary"
                onClick={onDirectoryCreate}
                sx={{
                    p: 0
                }}
            >
                <AddBoxSharp />
            </IconButton>
        </Box>
    );
};

export default CreateDirectoryFormField;
