import { React, useEffect } from 'react';
import { useForm } from 'react-hook-form';

export const GrandPrixYearSelector = ({ parentCallback, years }) => {

  const { register, handleSubmit, setValue } = useForm({
    
    defaultValues: { year: years[0] }
  });
  useEffect(() => {
    setValue("year", years[0])
  }, [years]);

  const onSubmit = data => {
    parentCallback(data)
  };

  return (
    <div className="GrandPrixYearSelector">
      <form onSubmit={handleSubmit(onSubmit)}>
        <select {...register("year")}>
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

