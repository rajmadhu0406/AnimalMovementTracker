import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ErrorService } from '../services/error.service';
import { ErrorMessageComponent } from '../components/error-message/error-message.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [RouterOutlet, ErrorMessageComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {
  constructor(public errorService: ErrorService) {}
}
