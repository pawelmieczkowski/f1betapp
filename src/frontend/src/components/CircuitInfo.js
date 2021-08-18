import "./CircuitInfo.scss";
import React, { useEffect, useRef } from 'react';

import Map from 'ol/Map'
import View from 'ol/View'
import TileLayer from 'ol/layer/Tile'
import OSM from 'ol/source/OSM';
import VectorLayer from 'ol/layer/Vector'
import VectorSource from 'ol/source/Vector'
import { RegularShape, Fill, Style } from 'ol/style';
import { Feature } from 'ol/index';
import { Point } from 'ol/geom';


export const CircuitInfo = ({ circuit }) => {
    const mapElement = useRef()

    useEffect(() => {
        const point = new Point([circuit.longitude, circuit.latitude]);

        const view = new View({
            projection: 'EPSG:4326',
            center: [50, 0],
            zoom: 14
        })
        new Map({
            target: mapElement.current,
            layers: [
                new TileLayer({
                    source: new OSM()
                }),
                new VectorLayer({
                    source: new VectorSource({
                        features: [new Feature(point)],
                    }),
                    style: new Style({
                        image: new RegularShape({
                            points: 3,
                            radius: 9,
                            fill: new Fill({ color: '#bf1650' }),
                        }),
                    }),
                }),
            ],
            view: view,
            controls: []
        })
        view.setCenter([circuit.longitude, circuit.latitude])
    }, [circuit])



    return (
        <section className="CircuitInfo">
            <div ref={mapElement} className="map"/>
            <div className="circuit-name">
                {circuit.name}
            </div>
            <div className="circuit-wikipage">
                <a href={circuit.url} target="_blank" rel="noopener noreferrer">Article on wikipedia</a>
            </div >
        </section>
    );
};