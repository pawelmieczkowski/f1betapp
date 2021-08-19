import "./CircuitInfo.scss";
import 'ol/ol.css';
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
        if (circuit.id) {
            new Map({
                target: mapElement.current,
                zIndex: -1,
                layers: [
                    new TileLayer({
                        className: "map-layer",
                        source: new OSM(),
                        zIndex: 1,
                    }),
                    new VectorLayer({
                        className: "marker-layer",
                        zIndex: 2,
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

            })
            view.setCenter([circuit.longitude, circuit.latitude])
        }
    }, [circuit])



    return (
        <section className="CircuitInfo">
            <div className="name">
                {circuit.name}
            </div>
            <div ref={mapElement} className="map" />
            <section className="circuit-info">
                <div className="circuit-localization">
                    <h2>
                        Circuit Localization:
                    </h2>
                    <div>
                        {circuit.location}, {circuit.country}
                    </div>
                </div>
                <div className="circuit-wikipage">
                    <a href={circuit.url} target="_blank" rel="noopener noreferrer">Read more on wikipedia</a>
                </div >
            </section>
        </section>
    );
};