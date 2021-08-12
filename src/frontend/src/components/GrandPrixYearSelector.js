import './GrandPrixYearSelector.scss'

import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

import { React, useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import Slider from "react-slick";


export const GrandPrixYearSelector = ({ parentCallback, years, yearSelected }) => {

  const [isDesktop, setDesktop] = useState(window.innerWidth > 800);

  const updateMedia = () => {
    setDesktop(window.innerWidth > 800);
  };

  useEffect(() => {
    window.addEventListener("resize", updateMedia);
    return () => window.removeEventListener("resize", updateMedia);
  });


  const { register, handleSubmit, setValue } = useForm({
    defaultValues: { year: years[0] }
  });

  useEffect(() => {
    setValue("year", years[0])
  }, [years, setValue]);

  const onSubmit = data => {
    parentCallback(data.year)
  };
  const submitButton = data => {
    parentCallback(data)
  }

  const settings = {
    dots: false,
    infinite: false,
    speed: 100,
    slidesToShow: 20,
    slidesToScroll: 20,
    initialSlide: 0,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 10,
          slidesToScroll: 10,
          infinite: true,
          dots: true
        }
      },
    ]
  };

  return (
    <div className="GrandPrixYearSelector">
      {isDesktop ? (
        <div className="slider">
          <Slider {...settings}>
            {years.map(year =>
              <button className={yearSelected === year ? "button-selected" : ""} value={year} key={years.length + year} onClick={() => submitButton(year)}>
                {year}
              </button>)
            }
          </Slider>
        </div>
      ) : (
        <form onSubmit={handleSubmit(onSubmit)}>
          <select className="form-select" {...register("year")}>
            {years.map(year =>
              <option value={year} key={years.length +  year}>
                {year}
              </option>)
            }
          </select>
          <input type="submit" value="Submit" className="form-select-button" />
        </form>
      )}
    </div>
  );
}

