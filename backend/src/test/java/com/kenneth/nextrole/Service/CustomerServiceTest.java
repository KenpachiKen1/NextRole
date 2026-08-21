package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Customer;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.CustomerRepository;
import com.kenneth.nextrole.SubscriptionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void createCustomer_savesCustomer_withFreeStatusAndGivenStripeId() {
        User user = User.builder().id(1L).username("kenneth").email("kenneth@example.com").build();

        Customer created = customerService.createCustomer(user, "cus_ABC123");

        assertThat(created.getUser()).isEqualTo(user);
        assertThat(created.getStripeCustomerId()).isEqualTo("cus_ABC123");
        assertThat(created.getSubscriptionStatus()).isEqualTo(SubscriptionStatus.FREE);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());
        assertThat(captor.getValue().getStripeCustomerId()).isEqualTo("cus_ABC123");
    }

    @Test
    void getByStripeCustomerId_returnsCustomer_whenFound() {
        Customer customer = Customer.builder().stripeCustomerId("cus_ABC123").build();
        when(customerRepository.findByStripeCustomerId("cus_ABC123"))
                .thenReturn(Optional.of(customer));

        Customer result = customerService.getByStripeCustomerId("cus_ABC123");

        assertThat(result).isEqualTo(customer);
    }

    @Test
    void getByStripeCustomerId_throwsRuntimeException_whenNotFound() {
        when(customerRepository.findByStripeCustomerId("cus_UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getByStripeCustomerId("cus_UNKNOWN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void updateSubscriptionStatus_updatesAndSaves() {
        Customer customer = Customer.builder().subscriptionStatus(SubscriptionStatus.FREE).build();

        customerService.updateSubscriptionStatus(customer, SubscriptionStatus.SUBSCRIBED);

        assertThat(customer.getSubscriptionStatus()).isEqualTo(SubscriptionStatus.SUBSCRIBED);
        verify(customerRepository).save(customer);
    }

    @Test
    void setSubscriptionId_setsAndSaves() {
        Customer customer = Customer.builder().build();

        customerService.setSubscriptionId(customer, "sub_XYZ789");

        assertThat(customer.getStripeSubscriptionId()).isEqualTo("sub_XYZ789");
        verify(customerRepository).save(customer);
    }
}