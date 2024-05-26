#include <BLEDevice.h>
//its work
BLEServer* pServer = NULL;
BLECharacteristic* pCharacteristic = NULL;
int sensorValue = 0;  // Replace this with your actual sensor reading

void setup() {
  Serial.begin(115200);
  BLEDevice::init("BTAG");

  pServer = BLEDevice::createServer();
  BLEService* pService = pServer->createService("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
  pCharacteristic = pService->createCharacteristic(
      "beb5483e-36e1-4688-b7f5-ea07361b26a8",
      BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_NOTIFY
  );
  pCharacteristic->setValue("Hello World");
  pService->start();

  BLEAdvertising* pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(pService->getUUID());
  pAdvertising->setScanResponse(true);
  pAdvertising->setMinPreferred(0x06);  // functions that help with iPhone connections issue
  pAdvertising->setMinPreferred(0x12);
  BLEDevice::startAdvertising();
}

void loop() {
  // Update the sensor value
  // Replace this with your actual sensor reading logic
  sensorValue = digitalRead(2);

  // Update the BLE characteristic with the sensor value
  pCharacteristic->setValue(String(sensorValue).c_str());
  pCharacteristic->notify();

  delay(1000);  // Adjust the delay as needed
}
