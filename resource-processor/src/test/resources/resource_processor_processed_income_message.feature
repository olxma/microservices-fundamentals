Feature: Resource Processor processed income message
  This is an example of component test
  Scenario: Resource Processor received event
    Given Resource creation event occurs on Resource Service side
    When Resource Service sends event
    Then Resource Processor handles income event and sends metadata to Song Service
