import React from "react";
import { useNavigate, useLocation } from "react-router-dom";

export default function Unauthorized() {
  const navigate = useNavigate();
  const location = useLocation();

  const goBack = () => {
    if (location.state?.from) {
      navigate(location.state.from);
    } else {
      navigate(-1);
    }
  };

  return (
    <main>
      <h1>Unauthorized</h1>

      <button onClick={goBack} className="btn btn-primary">
        Go back
      </button>
    </main>
  );
}
