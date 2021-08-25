import './Error.scss'
import React from "react";

export const Page404 = () => (
  <div className='Error'>
    <div className='wrap'>
      <div className='status'>
        <p>
          Error
        </p>
        <p className='code'>
          404
        </p>
      </div>
      <div className='message'>Page Not Found</div>
    </div>
  </div>
);
