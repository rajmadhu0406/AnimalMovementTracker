import { Component, EventEmitter, Output } from '@angular/core';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-no-permission-modal',
  imports: [],
  templateUrl: './no-permission-modal.component.html',
  styleUrl: './no-permission-modal.component.scss',
  standalone: true
})
export class NoPermissionModalComponent {
  constructor(public activeModal: NgbActiveModal) { }

  closeModal(): void {
    this.activeModal.close();
  }
}