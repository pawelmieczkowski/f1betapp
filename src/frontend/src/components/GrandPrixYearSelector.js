import { React, setValue } from 'react';
import { useForm } from 'react-hook-form';

export const GrandPrixYearSelector = ({ parentCallback, years }) => {

  console.log()
  const { register, handleSubmit } = useForm({
    //TODO: make 2021 dynamic
    defaultValues: { year: "2021" }
  });
  const onSubmit = data => {
    parentCallback(data)
  };

  return (
    <div className="GrandPrixYearSelector">
      <form onSubmit={handleSubmit(onSubmit)}>
        <select ref="register" {...register("year")}>
          {years.map(year =>
            <option value={year} key={year}>
              {year}
            </option>)
          }
        </select>
        <input type="submit" />
      </form>
    </div>
  );
}

