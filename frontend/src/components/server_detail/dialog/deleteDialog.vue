<template>
  <v-row justify="center">
    <v-dialog v-model="visible" persistent max-width="600px">
      <v-card>
        <v-card-title>
          DELETE
        </v-card-title>
        <v-card-text>
          <v-container>
            <v-row>
              <v-col>
                key : {{selectKey}}
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                value : {{value}}
              </v-col>
            </v-row>
          </v-container>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="deleteData">Delete</v-btn>
          <v-btn color="blue darken-1" text @click="closeDialog">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: 'deleteTtlDialog',
  props: {
    visible: {
      type: Boolean,
      required: true
    },
    selectKey: {
      type: String
    },
    value: {
      type: String
    }
  },
  methods: {
    deleteData () {
      this.$emit('delete-data')
    },
    closeDialog () {
      this.$emit('close-dialog')
    }
  }
}
</script>

<style scoped>

</style>
