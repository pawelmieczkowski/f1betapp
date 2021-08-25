import { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";

export const useQuery = ({ url }) => {
    const history = useHistory();
    const [apiData, setApiData] = useState([]);

    useEffect(() => {
        const fetchDrivers = async () => {
            const response = await fetch(url);
            if (!response.ok) {
                let err = new Error(response.statusText)
                err.code = response.status;
                throw err;
            } else {
                const data = await response.json();
                setApiData(data);
            }
        };
        fetchDrivers().catch(error => {
            const code = error.code ? error.code : 'NetworkError'
            history.replace(history.location.pathname, {
                errorStatusCode: code
            });
        });
    }, [url, history]);

    return { data: apiData };
};
