import './Error.scss'
import React from "react";

export const Error = ({code}) => (
  <div className='Error'>
    <div className='wrap'>
      <div className='status'>
        <p>
          Error
        </p>
        <p className='code'>
          {code}
        </p>
      </div>
      <div className='message'>Page Not Found</div>
    </div>
  </div>
);
