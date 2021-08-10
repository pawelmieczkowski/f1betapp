import "./RegisterPage.scss"
import { React, useState } from "react";
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as Yup from 'yup';

import AuthService from "../services/auth.service";


export const Register = () => {

    const validationSchema = Yup.object().shape({
        username: Yup.string()
            .required('Username is required')
            .min(4, 'Username must be at least 4 characters')
            .max(20, 'Username must not exceed 20 characters'),
        email: Yup.string()
            .required('Email is required')
            .email('Email is invalid'),
        password: Yup.string()
            .required('Password is required')
            .min(4, 'Password must be at least 4 characters')
            .max(40, 'Password must not exceed 40 characters'),
        confirmPassword: Yup.string()
            .required('Confirm Password is required')
            .oneOf([Yup.ref('password'), null], 'Confirm Password does not match'),
    });

    const [successful, setSuccessful] = useState(false);
    const [message, setMessage] = useState("");

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm({
        resolver: yupResolver(validationSchema)
    });

    const onSubmit = (data) => {

        setMessage("");
        setSuccessful(false);

        AuthService.register(data.username, data.email, data.password).then(
            (response) => {
                console.log(response)
                setMessage(response.data);
                setSuccessful(true);
            },
            (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data) ||
                    error.message ||
                    error.toString();
                setMessage(resMessage);
                setSuccessful(false);
            }
        );
    };

    return (
        <div className="RegisterPage">
            <div className="form-group">
                <h1 className="headline">
                    Create Account
                </h1>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className="form-field">
                        <input
                            placeholder="Username"
                            name="username"
                            type="text"
                            {...register('username')}
                            className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                        />
                        <div className="invalid-feedback">{errors.username?.message}</div>
                    </div>

                    <div className="form-field">
                        <input
                            placeholder="Email"
                            name="email"
                            type="text"
                            {...register('email')}
                            className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                        />
                        <div className="invalid-feedback">{errors.email?.message}</div>
                    </div>

                    <div className="form-field">
                        <input
                            placeholder="Password"
                            name="password"
                            type="password"
                            {...register('password')}
                            className={`form-control ${errors.password ? 'is-invalid' : ''}`}
                        />
                        <div className="invalid-feedback">{errors.password?.message}</div>
                    </div>

                    <div className="form-field">
                        <input
                            placeholder="Confirm password"
                            name="confirmPassword"
                            type="password"
                            {...register('confirmPassword')}
                            className={`form-control ${errors.confirmPassword ? 'is-invalid' : ''
                                }`}
                        />
                        <div className="invalid-feedback">
                            {errors.confirmPassword?.message}
                        </div>
                    </div>

                    <div className="form-field">
                        <button type="submit" className="register-button">
                            Register
                        </button>
                    </div>
                </form>

                {message && (
                    <div
                        className={
                            successful ? "alert alert-success" : "alert alert-danger"
                        }
                        role="alert"
                    >
                        {message}
                    </div>
                )}
            </div>
        </div>
    );
};
