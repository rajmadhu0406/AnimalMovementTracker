import { Component } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MainComponent } from './main/main.component';
import { RouterModule } from '@angular/router'; // Import RouterModule


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [NavbarComponent, MainComponent, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'frontend';
}
