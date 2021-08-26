import React from "react";
import { useLocation } from "react-router-dom";
import { get } from "lodash";

import { Page404 } from "../../pages/error/Page404"
import { Error } from "../../pages/error/Error"
import { NetworkError } from "../../pages/error/NetworkError"

const ErrorHandler = ({ children }) => {
    const location = useLocation();

    const code = get(location.state, "errorStatusCode")
    switch (code) {
        case 404:
            return <Page404/>;
            case 400:
                return <Error code={code} />;
        case 'NetworkError':
            return <NetworkError />;
        default:
            return children;
    }
};

export default ErrorHandler;
