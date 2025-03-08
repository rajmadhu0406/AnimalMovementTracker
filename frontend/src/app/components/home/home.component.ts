import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from 'express';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  jwtPayload: any;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.jwtPayload = this.authService.getJwtPayload();
  
    //console.log jwtPayload if not null
    if (this.jwtPayload) {
      console.log(this.jwtPayload);
    }
    else{
      console.log('jwtPayload is null');
    }

  }

}
