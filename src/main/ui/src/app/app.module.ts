import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule} from '@angular/common/http';
// Components
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';

// Material Dialog Stuff
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelect, MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatIconModule } from '@angular/material/icon';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { CreateBottleModalComponent } from './modals/create-bottle-modal/create-bottle-modal.component';
import { CreatePartialBottleModalComponent } from './modals/create-partial-bottle-modal/create-partial-bottle-modal.component';
import { AdminInventoryModalComponent } from './modals/admin-inventory-modal/admin-inventory-modal.component';
import { NgxSliderModule } from '@angular-slider/ngx-slider';
import { CreateUserModalComponent } from './modals/create-user-modal/create-user-modal.component';
import { RootDashboardComponent } from './root-dashboard/root-dashboard.component';
import { CreateCompanyModalComponent } from './modals/create-company-modal/create-company-modal.component';
import { CreateUserRootModalComponent } from './modals/create-user-root-modal/create-user-root-modal.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    HomeComponent,
    AdminDashboardComponent,
    CreateBottleModalComponent,
    CreatePartialBottleModalComponent,
    AdminInventoryModalComponent,
    CreateUserModalComponent,
    RootDashboardComponent,
    CreateCompanyModalComponent,
    CreateUserRootModalComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatIconModule,
    NgxSliderModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
