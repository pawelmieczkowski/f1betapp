import { useState, useEffect } from "react";
import { useHistory } from "react-router-dom";

export const useQuery = ({ url }) => {
    const history = useHistory();
    const [apiData, setApiData] = useState([]);

    useEffect(() => {
        let isMounted = true;
        const fetchData = async () => {
            const response = await fetch(url);
            if (!response.ok) {
                let err = new Error(response.statusText)
                err.code = response.status;
                throw err;
            } else {
                const data = await response.json();
                if (isMounted) {
                    setApiData(data);
                }
            }
        };
        fetchData().catch(error => {
            const code = error.code ? error.code : 'NetworkError'
            history.replace(history.location.pathname, {
                errorStatusCode: code
            });
        });
        return function cleanup() {
            isMounted = false
        }
    }, [url, history]);

    return { data: apiData };
};
