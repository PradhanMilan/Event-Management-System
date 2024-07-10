import AdminHeader from "./AdminHeader";
import HeaderCustomer from "./HeaderCustomer";
import NormalHeader from "./NormalHeader";

const RoleNav = () => {
  const customer = JSON.parse(sessionStorage.getItem("active-customer"));
  const admin = JSON.parse(sessionStorage.getItem("active-admin"));

  if (customer != null) {
    return <HeaderCustomer />;
  } else if (admin != null) {
    return <AdminHeader />;
  } else {
    return <NormalHeader />;
  }
};

export default RoleNav;
