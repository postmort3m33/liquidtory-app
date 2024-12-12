import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { RootDashboardComponent } from './root-dashboard/root-dashboard.component';

const routes: Routes = [
  { path: '', component: HomeComponent },  // Default route (e.g., login page)
  { path: 'home', component: HomeComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'admin', component: AdminDashboardComponent },
  { path: 'root', component: RootDashboardComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
