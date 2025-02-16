import { Button, Card, CardMedia, Container, Typography } from "@mui/material"
import Grid from "@mui/material/Grid2";
import { useUserInfo } from "../hooks/useUserInfo";
import { useNavigate } from "react-router-dom";
import { Google } from "@mui/icons-material";
import { getOauth2URL } from "../constants";


const HomePage = () => {

	const imageCount = 6;
	const { userInfo } = useUserInfo();

  	return (
		<Container sx={{ mt: "84px" }}>
			<Container maxWidth="lg" sx={{ textAlign: "center", padding: 4 }}>
				{/* Header */}
				<Typography variant="h3" fontWeight="bold" gutterBottom>
					Welcome to KAMA's Photo Library
				</Typography>
				<Typography variant="h6" color="text.secondary" paragraph>
					Organize, explore, and showcase your memories in one place.
				</Typography>
				
				{/* Hero Section */}
				{
					userInfo ? <LibraryButton /> : <GoogleLoginButton />
				}
				
				{/* Gallery Preview */}
				<Grid container spacing={2} justifyContent="center" sx={{ marginTop: 4 }} >
					{
						Array.from(Array(imageCount).keys()).map((_, index) => 
							<Grid item key={index}>
								<Card>
									<CardMedia 
										component="img" 
										image={`https://unsplash.it/300/200?random${index}`}
										alt={`Gallery ${index}`} 
										sx={{ 
											width: 300, 
											height: 200 
										}} 
									/>
								</Card>
							</Grid>
						)
					}
				</Grid>
			</Container>
		</Container>
	);
};

const LibraryButton = () => {

	const navigate = useNavigate();

	return (
		<Button
			variant="contained"
			size="large"
			color="primary"
			onClick={() => navigate("/library")}
		>
			Visit your Library
		</Button>
	);
};

const GoogleLoginButton = () => {
	return (
		<Button
			variant="contained"
			color="primary"
			size="large"
			sx={{
				color: 'white',
				bgcolor: '#4285F4',
				textTransform: 'none',
				width: '200px'
			}}
			startIcon={<Google />}
			onClick={() => window.location.href = getOauth2URL("google")}
		>
			Login with Google
		</Button>
	);
};

export default HomePage;
