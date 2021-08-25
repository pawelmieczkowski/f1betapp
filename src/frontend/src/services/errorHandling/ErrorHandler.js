import React from "react";
import { useLocation } from "react-router-dom";
import { get } from "lodash";

import { Page404 } from "../../pages/error/Page404"
import { NetworkError } from "../../pages/error/NetworkError"

const ErrorHandler = ({ children }) => {
    const location = useLocation();

    switch (get(location.state, "errorStatusCode")) {
        case 404:
            return <Page404 />;
        case 'NetworkError':
            return <NetworkError />;
        default:
            return children;
    }
};

export default ErrorHandler;
