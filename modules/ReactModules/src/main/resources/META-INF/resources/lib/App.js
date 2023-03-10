import React from "react";
import {Form} from "react-bootstrap";

class App extends React.Component {
    render() {
        return(

            <Form>

                <Form.Group className="mb-3" controlId="formBasicEmail">
                    <Form.Label>Email address</Form.Label>
                    <Form.Control type="email" placeholder="Enter email" />
                    <Form.Text className="text-muted">
                        We'll never share your email with anyone else.
                    </Form.Text>
                </Form.Group>
            </Form>
        )
    }
}

export default App;