import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Order} from "./order";

@Component({
  moduleId: module.id,
  selector: 'freebird-filters-order',
  templateUrl: 'filter-order.component.html'
})
export class FilterOrderComponent {

  @Input()
  orders: Order[];

  @Output()
  orderByEvent = new EventEmitter<any>();

  @Input()
  selectedOrder: Order;

  orderByClicked(order: Order) {
    this.selectedOrder = order;
    this.orderByEvent.emit(order);
  }
}
