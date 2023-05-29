import React from "react";
import useAuth from "../hooks/useAuth";
import { Outlet, useLocation, Navigate } from "react-router-dom";
import DashboardLayout from "../Layout/DashboardLayout";

const RequireAuth = ({ allowedRoles }) => {
  const { auth } = useAuth();
  const location = useLocation();

  console.log(allowedRoles);
  console.log(auth.role);

  console.log(allowedRoles === auth.role);

  return allowedRoles?.includes(auth?.role) ? (
    <Outlet />
  ) : auth?.user ? (
    <Navigate to="/unauthorized" state={{ from: location }} />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default RequireAuth;
