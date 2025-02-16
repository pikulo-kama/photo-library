import { Breadcrumbs, Link, Typography } from "@mui/material";
import { FaFolder } from "react-icons/fa";


const DirectoryPath = ({ directoryPath }) => {

    return (
        <Breadcrumbs
            sx={{
                px: 3,
                py: 0.5,
                bgcolor: "secondary.dark"
            }}
        >
            {
                directoryPath.map((directory, index) =>
                    <Link 
                        underline="hover" 
                        color="inherit" 
                        key={index}
                        href={`/library/${directory.directoryUUID}`}
                    >
                        <Typography
                            color="text.primary"
                            sx={{
                                display: "flex",
                                alignItems: "center",
                                gap: 1,
                                fontSize: "18px"
                            }}
                        >
                            <FaFolder />
                            {directory.directoryName}
                        </Typography>
                    </Link>        
                )
            }
        </Breadcrumbs>
    );
};

export default DirectoryPath;
