float colision(Car r, float Dinit, boolean acc) {
  float acc= 100.0f/(1.4f*36.0f);
  float desc= 100.0f/(36.0f);
  
  
  // Cas où on freine  et la voiture devant accélère
  
  
  if (isBreaking) {
    if (!acc) {
      return -1;
    }
    
    if (r.isBreaking() || r.estalarrer()) {
     
      return -1;
      
    }else {
      float dbreak=speed*speed/(desc*2);
      if (dbreak<=Dinit) {
         return 0.00000001f;
      }
      
     
      float tvMax = (MaxSpeed - speed)/ acc;
      float delta = r.getSpeed()*r.getSpeed() - 2*acc*(Dinit-(getSpeed()*getSpeed())/(desc*2)));
      float t = (-r.getSpeed()+Math.sqrt(delta))/acc;
      
      if (t<=tvMax) {
        if (t<=1.0f) {
          return 1.0f
        }
        return t;
      } else {
        float dA = tvMax*tvMax*acc/2+r.getSpeed()*tvMax+Dinit-MaxSpeed*tvMax;
        t =(speed*speed/(2*desc)-dA)/MaxSpeed;
        if (t<=1.0f) {
          return 1.0f
        }
        return t;

      }
      
      
    }
    
    
                       
  }else if (getSpeed()<MaxSpeed) {
    if (acc) {
      return -1;
    }
    if (r.isBreaking() || r.estalarrer()) {
      return aux(r,Dinit);
    } else if (r.getSpeed()<MaxSpeed) {
      
    
    }else {
    }
    
  }else {
    if (acc) {
      return -1;
    }
     if (r.isBreaking() || r.estalarrer()) {
        return aux(r,Dinit);
     }
    else if (r.getSpeed()<MaxSpeed) {
      
    
          }else {
    }
    
  }
}

 ////////////////////////////////////////////////////////////////////////////////////////////////////////     
      
     // Cas où la voiture de devant accélère et nous aussi.
      
    float frein(Car r, float Dinit){
      float acc= 100.0f/(1.4f*36.0f);
      float desc= 100.0f/(36.0f);
      float TVmaxA = (MaxSpeed - getSpeed)/ acc;
      float TVmaxB = (MaxSpeed - r.getSpeed)/ acc;
      float XVmaxA = TVmaxA*(MaxSpeed+getSpeed)/2;
      float XVmaxB = TVmaxB*(MaxSpeed+r.getSpeed)/2;
      
        if(r.getSpeed()>=getSpeed()){
          return 100000;    // En gros la voiture de devant va plus vite donc on accélère jusqu'à atteindre la vitesse max et attendre un pochain évènement.
        }
        else{
          float Dfinal = XVmaxB - XVmaxA;
          if(Dfinal > 35){
            return 1000000;   // Pareil on ne fait rien
          }
          else {
            return 0.000001;
          }
        }
      
     //////////////////////////////////////////////////////////////////////////////////////////////////////// 

// cas ou la voiture de devant freine 

float aux1(Car r, float Dinit) {
   float acc= 100.0f/(1.4f*36.0f);
   float desc= 100.0f/(36.0f);
   float dFrB=r.getSpeed()*r.getSpeed()/(2*desc);
   float dFrA=getSpeed()*getSpeed()/(2*desc);
   return (Dinit+dFrB-dFrA)/getSpeed(); // apres voir avec plus 1
   
}
