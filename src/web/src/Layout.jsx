import { BrowserRouter, Route, Routes } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LibraryPage from "./pages/LibraryPage";
import { Provider } from "react-redux";
import { store } from "./store";
import RequireAuth from "./auth/RequireAuth";
import Header from "./components/Header";
import { ThemeProvider } from "@emotion/react";
import { theme } from "./theme";
import { Divider } from "@mui/material";


const Layout = () => (
    <>
        <Provider store={store}>
            <ThemeProvider theme={theme}>
                <BrowserRouter>
                    <Header />
                    <Divider />

                    <Routes>
                        <Route path="/" element={<HomePage />} />

                        <Route element={<RequireAuth />}>
                            <Route path="/library/:directory?" element={<LibraryPage />} />
                        </Route>
                    </Routes>
                </BrowserRouter>
            </ThemeProvider>
        </Provider>
    </>
);

export default Layout;
