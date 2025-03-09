export interface Movement {
    id?: number;
    accountCompany: string;
    newMovementReason: string;
    newSpecies: string;
    newOriginAddress: string;
    newOriginCity: string;
    newOriginName: string;
    newOriginPostalCode: string;
    newOriginPremId: string;
    newOriginState: string;
    newDestinationAddress: string;
    newDestinationCity: string;
    newDestinationName: string;
    newDestinationPostalCode: string;
    newDestinationPremId: string;
    newDestinationState: string;
    originLat: number;
    originLon: number;
    destinationLat: number;
    destinationLong: number;
    newNumItemsMoved: number;
    newShipmentsStartDate: string;
  }
  