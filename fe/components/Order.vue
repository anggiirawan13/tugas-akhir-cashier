<template>
  <v-row>
    <v-col cols="12">
      <h2>Order</h2>
      <v-list>
        <v-list-item v-for="(item, index) in cartItems" :key="index">
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
            <v-list-item-subtitle
              >{{ currency(item.price) }} X
              <v-btn dark :color="$vuetify.theme.themes.dark.secondary"
                icon
                class="px-0"
                x-small
                @click="decrement(item.id)"
              >
                <v-icon>mdi-chevron-down</v-icon>
              </v-btn>
              {{ item.quantity }}
              <v-btn dark :color="$vuetify.theme.themes.dark.secondary"
                icon
                class="px-0"
                x-small
                @click="increment(item.id)"
              >
                <v-icon>mdi-chevron-up</v-icon>
              </v-btn>
            </v-list-item-subtitle>
          </v-list-item-content>
          <v-list-item-action>
            <v-btn dark :color="$vuetify.theme.themes.dark.secondary" icon color="error" x-small @click="remove(item.id)">
              <v-icon>mdi-close-thick</v-icon>
            </v-btn>
            {{ currency(itemTotal(item.price, item.quantity)) }}
          </v-list-item-action>
        </v-list-item>
        <v-list-item class="text-h6 black--text grey lighten-2">
          <v-list-item-content>
            <v-list-item-title>Sub Total</v-list-item-title>
          </v-list-item-content>
          <v-list-item-action>
            {{ currency(subTotal) }}
          </v-list-item-action>
        </v-list-item>
        <v-list-group :value="false" class="black--text grey lighten-3">
          <template v-slot:activator>
            <v-list-item-content class="text-h6">
              <v-list-item-title>Additionals</v-list-item-title>
            </v-list-item-content>
          </template>
          <template v-for="(additional, index) in additionals">
            <v-list-item disabled class="black--text grey lighten-3">
              <v-list-item-content>
                <v-list-item-title>{{ additional.title }}</v-list-item-title>
                <v-list-item-subtitle v-if="additional.mode === 'percentage'"
                  >{{ currency(additional.value) }}%</v-list-item-subtitle
                >
              </v-list-item-content>
              <v-list-item-action>
                <v-list-item-title v-if="additional.mode === 'fix'">
                  {{ currency(additional.value) }}
                </v-list-item-title>
                <v-list-item-title v-else-if="additional.mode === 'percentage'">
                  {{ currency(calculatePercentage(additional.value)) }}
                </v-list-item-title>
              </v-list-item-action>
            </v-list-item>
          </template>
        </v-list-group>
        <v-list-item class="text-h6 black--text grey lighten-2">
          <v-list-item-content>
            <v-list-item-title>Total</v-list-item-title>
          </v-list-item-content>
          <v-list-item-action>
            {{ currency(total) }}
          </v-list-item-action>
        </v-list-item>
      </v-list>
      <v-btn dark :color="$vuetify.theme.themes.dark.secondary" block @click="checkOut()">Check Out</v-btn>
    </v-col>
  </v-row>
</template>

<script>
import { mapState, mapActions, mapGetters } from "vuex";
import Swal from 'sweetalert2';

export default {
  methods: {
    ...mapActions("cart", {
      increment: "increment",
      decrement: "decrement",
      remove: "remove",
    }),
    currency(value) {
      return Intl.NumberFormat("en-US").format(value);
    },
    checkOut() {
      const username = this.getUserLogin.name;
      const data = {
        username,
        sub_total: this.subTotal,
        total_net: this.total,
        tax: this.additionals[0].value,
        service_charge: this.additionals[1].value,
        item: [],
      };
      console.log(this.cartItems)
      for (let i = 0; i < this.cartItems.length; i++) {
        const temp = {
          product_id: parseInt(this.cartItems[i].id),
          quantity: this.cartItems[i].quantity,
          total_price: this.cartItems[i].price * this.cartItems[i].quantity,
          price: this.cartItems[i].price,
        };

        data.item.push(temp);
      }
      console.log(data)
      this.$axios
          .$post('/order', data)
          .then(resp => {
            Swal.fire({
              icon: 'success',
              title: 'Order Created!',
              text: 'Your order has been successfully created.',
              confirmButtonText: 'OK'
            }).then((res) => {
                  window.location.reload();
                });
          })
          .catch(err => {
            console.error(err);
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'An error occurred while creating the order.',
              confirmButtonText: 'OK'
            }).then((res) => {
              window.location.reload();
            });
          });

    },
  },
  computed: {
    ...mapState("cart", {
      items: "items",
      additionals: "additionals",
    }),
    ...mapGetters("cart", {
      cartItems: "cartItems",
      itemTotal: "itemTotal",
      subTotal: "subTotal",
      calculatePercentage: "calculatePercentage",
      total: "total",
    }),
    ...mapGetters("auth", {
      getUserLogin: "user",
    }),
  },
};
</script>
